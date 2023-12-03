import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useAddTicketMutation } from '../../../core/api/ticket';

type TicketTypeType = 'VIP' | 'USUAL' | 'BUDGETARY' | 'CHEAP';
type TicketVenueType = 'THEATRE' | 'CINEMA' | 'MALL' | 'STADIUM';

const keys: { value: string; type: 'text' | 'number' }[] = [
    { value: 'name', type: 'text' },
    { value: 'coordinateX', type: 'number' },
    { value: 'coordinateY', type: 'number' },
    { value: 'price', type: 'number' },
    { value: 'type', type: 'text' },
    { value: 'venueName', type: 'text' },
    { value: 'venueCapacity', type: 'number' },
    { value: 'venueType', type: 'text' },
    { value: 'personId', type: 'number' },
];

interface IDataType {
    coordinateX: number;
    coordinateY: number;
    name: string;
    personId: number;
    price: number;
    type: TicketTypeType;
    venueCapacity: number;
    venueName: string;
    venueType: TicketVenueType;
}

export const AddTicketForm: React.FC = () => {
    const { register, handleSubmit } = useForm();
    const [addTicket] = useAddTicketMutation();
    const [error, setError] = useState('');
    const handleAddTicket = async (data: IDataType) => {
        await addTicket({
            name: data.name,
            type: data.type,
            personId: data.personId,
            price: data.price,
            coordinates: {
                x: data.coordinateX,
                y: data.coordinateY,
            },
            venue: {
                name: data.venueName,
                type: data.venueType,
                capacity: data.venueCapacity,
            },
        })
            .unwrap()
            .then(() => setError(''))
            .catch(() => setError('server error'));
    };

    return (
        <form
            className={'grid h-min w-60 grid-flow-row gap-2 rounded-2xl bg-light_purple p-5'}
            onSubmit={handleSubmit(handleAddTicket)}
        >
            {keys.map((x) => {
                return (
                    <input
                        className={'h-[26px] w-full rounded border bg-light_purple pl-2 placeholder-gray'}
                        key={x.value}
                        {...register(x.value)}
                        type={x.type}
                        placeholder={x.value}
                    />
                );
            })}
            <button type={'submit'} className={'mt-2 h-[26px] w-28 rounded bg-purple'}>
                Add Ticket
            </button>
            <div className={'flex h-[30px] items-center justify-center text-red'}>{error}</div>
        </form>
    );
};
